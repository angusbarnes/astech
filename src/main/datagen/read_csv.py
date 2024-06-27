import csv


def get_chemical_defs(filename):
        # Open the CSV file
    with open(file_path, mode='r') as file:
        # Create a CSV reader object
        csv_reader = csv.DictReader(file)
        
        return (csv_reader.fieldnames, [row for row in csv_reader])


# Define the file path
file_path = 'chems.csv'
header, rows = get_chemical_defs(file_path)

# Read and print the rows
for row in rows:
    print("Row:")
    for field in header:
        print(f"  {field}: {row[field]}")
    print()