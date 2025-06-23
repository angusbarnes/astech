import json

facing_y = {
    "south": 180,
    "west": 270,
    "north": 0,
    "east": 90
}

multipart = []
part_files = [

"brick_kiln_one",
"brick_kiln_two",
"brick_kiln_three",
"brick_kiln_four",
"brick_kiln_five",
"brick_kiln_six",
"brick_kiln_seven",
"brick_kiln_eight",
"brick_kiln_nine",
"brick_kiln_ten",
"brick_kiln_eleven",
"brick_kiln_twelve",
"brick_kiln_thirteen",
"brick_kiln_fourteen",
"brick_kiln_fifteen",
"brick_kiln_sixteen",
"brick_kiln_seventeen",
"brick_kiln_eighteen",
]

for i in range(1, 19):  # 1 to 18
    for facing, y in facing_y.items():
        entry = {
            "when": {
                "facing": facing,
                "brick_count": i
            },
            "apply": {
                "model": f"astech:block/kiln/brick_kiln_{part_files[i-1]}",
                "y": y
            }
        }
        multipart.append(entry)

blockstate = {
    "multipart": multipart
}

with open("brick_kiln_pile.json", "w") as f:
    json.dump(blockstate, f, indent=2)