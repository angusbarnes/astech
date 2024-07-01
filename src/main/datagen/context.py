import os
import json

class Context:
    def __init__(self, resources_path) -> None:
        self.ROOT_FOLDER = resources_path
        self.TAGS = {}
        self.MASTER_TAGS = {}
        
    def add_data_folder(self, folder_name):
        folder_path = os.path.join(self.ROOT_FOLDER, "data", folder_name)
        os.makedirs(folder_path, exist_ok=True)
        return folder_path


    # This one takes a fully qualified tag ("minecraft:items/planks")
    def add_tag(self, tag_name, tag_value):
        namespace, *path_parts = tag_name.split(':')
        path = '/'.join(path_parts)
        
        if namespace not in self.TAGS:
            self.TAGS[namespace] = {}
        
        self.TAGS[namespace][path] = tag_value

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

    def write_to_disk(self):
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

context = Context("./resources")

context.add_item_tag("minecraft:planks/test", "minecraft:oak_planks")
context.add_item_tag("minecraft:planks", ["minecraft:spruce_planks", "minecraft:birch_planks"])
context.add_fluid_tag("minecraft:water", "minecraft:water")
context.add_block_tag("minecraft:logs", ["minecraft:oak_log", "minecraft:spruce_log"])

context.add_item_tag("forge:ingots/iron", "minecraft:iron_ingot")
context.add_item_tag("forge:ingots/gold", "minecraft:gold_ingot")
context.add_item_tag("forge:gems/diamond", "minecraft:diamond")

# Write all tags to disk
context.write_to_disk()