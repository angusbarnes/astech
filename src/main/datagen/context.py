import os
import json
import textwrap
from ResourcePackManager import ResourcePackManager

class Context:
    def __init__(self, resources_path, namespace) -> None:
        self.ROOT_FOLDER = resources_path
        self.TAGS = {}
        self.MASTER_TAGS = {}
        self.MODEL_DEFS = {}
        self.RECIPES = {}
        self.NAMESPACE = namespace
        self.LANG_KEYS = {}
        self.REGION_TEXT = {}
        self.BLOCK_STATES = {}
        self.LOOT_TABLES = {}

        # Ensures the required folders are created
        ResourcePackManager(resources_path, 'astech', {
            'assets': [
                'lang',
                'textures/item',
                'textures/block',
                'models/item',
                'models/block',
                'blockstates',
                'recipes/smelting'
            ],
            'data': [
                'recipes/smelting',
                'recipes/crafting'
                'recipes/compat',
                'loot_tables/blocks'
            ]
        })
        
    def add_data_folder(self, folder_name):
        folder_path = os.path.join(self.ROOT_FOLDER, "data", folder_name)
        os.makedirs(folder_path, exist_ok=True)
        return folder_path
    
    def add_translation(self, key, value):
        self.LANG_KEYS[key] = value

    def add_text_to_region(self, file, region, text):

        if (file, region) not in self.REGION_TEXT:
            self.REGION_TEXT[(file, region)] = []

        self.REGION_TEXT[(file, region)].append(text)

    def add_simple_item_model(self, item_name):
        self.MODEL_DEFS['item/' + item_name] = f"{{\"parent\": \"item/generated\",\"textures\":{{\"layer0\": \"{self.NAMESPACE}:item/{item_name}\"}}}}"

    def add_simple_block_model(self, block_name):
        self.MODEL_DEFS['block/' + block_name] = f"{{\"parent\": \"minecraft:block/cube_all\",\"textures\":{{\"all\": \"{self.NAMESPACE}:block/{block_name}\"}}}}"
        self.BLOCK_STATES[block_name] = f"""{{"variants": {{"": {{"model": "astech:block/{block_name}"}}}}}}"""

    def add_block_item_model(self, block_id):
        self.MODEL_DEFS['item/' + block_id] = f"""{{"parent": "astech:block/{block_id}"}}"""

    def add_simple_block_loot(self, astech_block_id):
        self.LOOT_TABLES[f'blocks/{astech_block_id}'] = json.dumps({
            "type": "minecraft:block",
            "pools": [
                {
                "bonus_rolls": 0.0,
                "conditions": [
                    {
                    "condition": "minecraft:survives_explosion"
                    }
                ],
                "entries": [
                    {
                    "type": "minecraft:item",
                    "name": f"astech:{astech_block_id}"
                    }
                ],
                "rolls": 1.0
                }
            ]
        })

    def add_ore_block_loot(self, astech_block_id, astech_ore_item_id):
        self.LOOT_TABLES[f'blocks/{astech_block_id}'] = json.dumps({
            "type": "minecraft:block",
            "pools": [
                {
                "bonus_rolls": 0.0,
                "entries": [
                    {
                    "type": "minecraft:alternatives",
                    "children": [
                        {
                        "type": "minecraft:item",
                        "conditions": [
                            {
                            "condition": "minecraft:match_tool",
                            "predicate": {
                                "enchantments": [
                                {
                                    "enchantment": "minecraft:silk_touch",
                                    "levels": {
                                    "min": 1
                                    }
                                }
                                ]
                            }
                            }
                        ],
                        "name": f"astech:{astech_block_id}"
                        },
                        {
                        "type": "minecraft:item",
                        "functions": [
                            {
                            "enchantment": "minecraft:fortune",
                            "formula": "minecraft:ore_drops",
                            "function": "minecraft:apply_bonus"
                            },
                            {
                            "function": "minecraft:explosion_decay"
                            }
                        ],
                        "name": f"astech:{astech_ore_item_id}"
                        }
                    ]
                    }
                ],
                "rolls": 1.0
                }
            ]
        })

    def add_smelting_recipe(self, id, input, output):

        json_template = textwrap.dedent(f"""
            {{
            "type": "minecraft:smelting",
            "cookingtime": 200,
            "experience": 0.35,
            "ingredient": {{
                "tag": "{input}"
            }},
            "result": "{output}"
            }}
        """)

        # We will only have one recipe for each smelting
        self.RECIPES[id] = json_template


    #TODO: fix this so it actually behaves
    def add_generic_recipe(self, id, recipe):

        # We will only have one recipe for each smelting
        self.RECIPES[id] = recipe


    # This one takes a fully qualified tag ("minecraft:items/planks")
    def add_tag(self, tag_name, tag_value):
        namespace, *path_parts = tag_name.split(':')
        path = '/'.join(path_parts)
        
        if namespace not in self.TAGS:
            self.TAGS[namespace] = {}
        
        self.TAGS[namespace][path] = tag_value

    def set_base_dictionary(self, translations):
        self.LANG_KEYS = translations

    def add_item_tag(self, tag_name, tag_value):
        self._add_specific_tag("items", tag_name, tag_value)

    def add_fluid_tag(self, tag_name, tag_value):
        self._add_specific_tag("fluids", tag_name, tag_value)

    def add_block_tag(self, tag_name, tag_value):
        self._add_specific_tag("blocks", tag_name, tag_value)

    def _add_specific_tag(self, tag_type, tag_name, tag_value):
            namespace, path = tag_name.split(':', 1)
            full_path = f"{tag_type}/{path}"
            
            # Handle the specific tag
            if namespace not in self.TAGS:
                self.TAGS[namespace] = {}
            
            if full_path not in self.TAGS[namespace]:
                self.TAGS[namespace][full_path] = []
            
            if isinstance(tag_value, list):
                self.TAGS[namespace][full_path].extend(tag_value)
            else:
                self.TAGS[namespace][full_path].append(tag_value)

            # Handle the master tag
            path_parts = path.split('/')
            for i in range(1, len(path_parts)):
                master_path = f"{tag_type}/{'/'.join(path_parts[:i])}"
                if namespace not in self.MASTER_TAGS:
                    self.MASTER_TAGS[namespace] = {}
                if master_path not in self.MASTER_TAGS[namespace]:
                    self.MASTER_TAGS[namespace][master_path] = []
                master_tag_value = f"#{namespace}:{'/'.join(path_parts[:i+1])}"
                if master_tag_value not in self.MASTER_TAGS[namespace][master_path]:
                    self.MASTER_TAGS[namespace][master_path].append(master_tag_value)

    def make_block_mineable(self, astech_block_id):
        self.add_simple_block_loot(astech_block_id)
        self.add_block_tag(f"minecraft:mineable/pickaxe", f"astech:{astech_block_id}")

    @staticmethod
    def _insert_text_in_region(file_path, region, new_text):
        # Define the anchors
        start_anchor = f"//#anchor {region}"
        end_anchor = f"//#end {region}"
        
        # Read the content of the file
        with open(file_path, 'r') as file:
            lines = file.readlines()
        
        # Find the start and end indices of the region
        start_index = None
        end_index = None
        
        for i, line in enumerate(lines):
            if start_anchor in line:
                start_index = i
            elif end_anchor in line:
                end_index = i
                break
        
        if start_index is None or end_index is None:
            print("Anchors not found in the file.")
            return
        
        # Insert the new text
        lines = lines[:start_index + 1] + [new_text + "\n"] + lines[end_index:]
        
        # Write the modified content back to the file
        with open(file_path, 'w', encoding="utf-8") as file:
            file.writelines(lines)

    def write_to_disk(self):

        lines_written = 0
        for key in self.REGION_TEXT.keys():
            filename, region = key
            lines_to_write = self.REGION_TEXT[key]

            if not os.path.exists(filename):
                raise Exception(f"File {filename} does not exist to insert for region {region}")
            
            lines_written += len(lines_to_write)
            Context._insert_text_in_region(filename, region, '\n'.join(lines_to_write))

        print(f"Wrote {lines_written} lines to source code regions on disk")

        models_written = 0
        for model_location, model_info in self.MODEL_DEFS.items():
            with open(f'../resources/assets/astech/models/{model_location}.json', 'w') as model_file:
                model_file.write(model_info)
            models_written += 1
            
        print(f"Wrote {models_written} models to disk")

        tables_written = 0
        for table_location, table_info in self.LOOT_TABLES.items():
            with open(f'../resources/data/astech/loot_tables/{table_location}.json', 'w') as table_file:
                table_file.write(table_info)
            tables_written += 1

        print(f"Wrote {tables_written} loot tables to disk")

        states_written = 0
        for state_location, state_info in self.BLOCK_STATES.items():
            with open(f'../resources/assets/astech/blockstates/{state_location}.json', 'w') as state_file:
                state_file.write(state_info)
            states_written += 1
            
        print(f"Wrote {states_written} block states to disk")

        recipes_written = 0
        for recipe_id, recipe in self.RECIPES.items():
            directory = os.path.dirname(f'../resources/data/astech/recipes/{recipe_id}.json')
    
             # Create all directories in the path if they don't exist
            os.makedirs(directory, exist_ok=True)
            with open(f'../resources/data/astech/recipes/{recipe_id}.json', 'w') as recipe_file:
                recipe_file.write(recipe)
            
            recipes_written += 1
                
        print(f"Wrote {recipes_written} recipes to disk")



        with open(f'../resources/assets/astech/lang/en_us.json', 'w', encoding='utf-8') as lang_file:
            json.dump(self.LANG_KEYS, lang_file, ensure_ascii=False, indent=4)
                
        print("Language File has been written")

        # Write specific tags
        for namespace, tags in self.TAGS.items():
            for tag_path, tag_value in tags.items():
                file_path = os.path.join(self.ROOT_FOLDER, "data", namespace, "tags", f"{tag_path}.json")
                os.makedirs(os.path.dirname(file_path), exist_ok=True)
                with open(file_path, 'w') as f:
                    json.dump({"replace": False, "values": tag_value}, f, indent=2)

        # Write master tags
        for namespace, master_tags in self.MASTER_TAGS.items():
            for tag_path, tag_value in master_tags.items():
                file_path = os.path.join(self.ROOT_FOLDER, "data", namespace, "tags", f"{tag_path}.json")
                os.makedirs(os.path.dirname(file_path), exist_ok=True)

                # Read existing tag file if it exists
                existing_data = {"replace": False, "values": []}
                if os.path.exists(file_path):
                    with open(file_path, 'r') as f:
                        try:
                            existing_data = json.load(f)
                        except json.JSONDecodeError:
                            print(f"Warning: Could not parse existing file {file_path}. It will be overwritten.")

                # Merge existing values with new values
                existing_data["values"] = list(set(existing_data["values"] + tag_value))

                # Write updated tag file
                with open(file_path, 'w') as f:
                    json.dump(existing_data, f, indent=2)

        print("All tags have been written to disk.")

# context = Context("./resources")

# context.add_item_tag("minecraft:planks/test", "minecraft:oak_planks")
# context.add_item_tag("minecraft:planks", ["minecraft:spruce_planks", "minecraft:birch_planks"])
# context.add_fluid_tag("minecraft:water", "minecraft:water")
# context.add_block_tag("minecraft:logs", ["minecraft:oak_log", "minecraft:spruce_log"])

# context.add_item_tag("forge:ingots/iron", "minecraft:iron_ingot")
# context.add_item_tag("forge:ingots/gold", "minecraft:gold_ingot")
# context.add_item_tag("forge:gems/diamond", "minecraft:diamond")

# # Write all tags to disk
# context.write_to_disk()