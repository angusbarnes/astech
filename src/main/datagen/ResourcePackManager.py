import os

class ResourcePackManager:
    def __init__(self, root_folder, namespace):
        self.ROOT_FOLDER = root_folder
        self.NAMESPACE = namespace
        self.folder_structure = {
            'assets': [
                'lang',
                'textures/item',
                'models/item',
                'blockstates',
                'recipes/smelting'
            ],
            'data': [
                'recipes/smelting',
                'recipes/crafting'
            ]
        }
        self.ensure_folder_structure()

    def ensure_folder_structure(self):
        for pack_type, folders in self.folder_structure.items():
            for folder in folders:
                folder_path = os.path.join(self.ROOT_FOLDER, pack_type, self.NAMESPACE, folder)
                os.makedirs(folder_path, exist_ok=True)

    def get_folder_path(self, pack_type, *subfolders):
        return os.path.join(self.ROOT_FOLDER, pack_type, self.NAMESPACE, *subfolders)

# Usage:
resource_pack = ResourcePackManager('/path/to/root', 'your_namespace')

# To get a specific folder path:
lang_folder = resource_pack.get_folder_path('assets', 'lang')
smelting_recipes_folder = resource_pack.get_folder_path('data', 'recipes', 'smelting')
