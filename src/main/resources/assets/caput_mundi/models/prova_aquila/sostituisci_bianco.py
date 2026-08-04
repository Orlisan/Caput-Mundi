"""
Sostituisce tutti i pixel completamente bianchi (255,255,255) di un PNG
con un colore marrone a scelta. Mantiene la trasparenza (alpha) invariata.

Uso:
    python sostituisci_bianco.py input.png output.png
    python sostituisci_bianco.py input.png output.png --colore 101 63 26
"""

import sys
import argparse
from PIL import Image


def sostituisci_bianco(input_path, output_path, colore_marrone=(101, 63, 26)):
    img = Image.open(input_path).convert("RGBA")
    pixels = img.load()
    larghezza, altezza = img.size

    r_new, g_new, b_new = colore_marrone

    for y in range(altezza):
        for x in range(larghezza):
            r, g, b, a = pixels[x, y]
            if r == 255 and g == 255 and b == 255:
                pixels[x, y] = (r_new, g_new, b_new, a)

    img.save(output_path)
    print(f"Fatto. Salvato in: {output_path}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Sostituisce pixel bianchi con marrone in un PNG")
    parser.add_argument("input", help="Percorso PNG di input")
    parser.add_argument("output", help="Percorso PNG di output")
    parser.add_argument(
        "--colore",
        nargs=3,
        type=int,
        default=[101, 63, 26],
        metavar=("R", "G", "B"),
        help="Colore marrone da usare (default: 101 63 26)",
    )
    args = parser.parse_args()

    sostituisci_bianco(args.input, args.output, tuple(args.colore))
