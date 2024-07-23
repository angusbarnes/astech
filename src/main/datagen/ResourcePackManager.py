import os

class ResourcePackManager:
    def __init__(self, root_folder, namespace, structure):
        self.ROOT_FOLDER = root_folder
        self.NAMESPACE = namespace
        self.folder_structure = structure
        self.ensure_folder_structure()

    def ensure_folder_structure(self):
        for pack_type, folders in self.folder_structure.items():
            for folder in folders:
                folder_path = os.path.join(self.ROOT_FOLDER, pack_type, self.NAMESPACE, folder)
                os.makedirs(folder_path, exist_ok=True)

    def get_folder_path(self, pack_type, *subfolders):
        return os.path.join(self.ROOT_FOLDER, pack_type, self.NAMESPACE, *subfolders)