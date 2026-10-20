#!/usr/bin/env python3
"""Regenerates every platform's app icon from the Revera mark.

Usage:  python3 tools/brand/generate_icons.py
Needs:  rsvg-convert (brew install librsvg), Pillow (pip install pillow), macOS iconutil for .icns.

Edit BRAND below (or revera-mark.svg) and re-run to rebrand all platforms at once.
Android uses vector drawables (apps/android/src/main/res/drawable/ic_launcher_*.xml) — keep their
path data in sync with revera-mark.svg by hand; they are not generated.
"""
import io
import pathlib
import shutil
import subprocess
import tempfile

from PIL import Image

ROOT = pathlib.Path(__file__).resolve().parents[2]
HERE = pathlib.Path(__file__).resolve().parent
BRAND = {"from": "#6273F2", "to": "#3B44C4"}

MARK = (HERE / "revera-mark.svg").read_text()
MARK_BODY = MARK[MARK.index(">", MARK.index("<svg")) + 1 : MARK.rindex("</svg>")]


def svg(shape: str, inset: float, mark_scale: float) -> str:
    """Brand gradient background (full-bleed square or rounded tile) with the mark on top."""
    size = 108 - 2 * inset
    if shape == "square":
        bg = '<rect width="108" height="108" fill="url(#g)"/>'
    else:  # rounded tile, macOS-style proportions
        bg = f'<rect x="{inset}" y="{inset}" width="{size}" height="{size}" rx="{size * 0.225}" fill="url(#g)"/>'
    return f"""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108">
  <defs><linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
    <stop offset="0" stop-color="{BRAND['from']}"/><stop offset="1" stop-color="{BRAND['to']}"/>
  </linearGradient></defs>
  {bg}
  <g transform="translate(54 54) scale({mark_scale}) translate(-54 -54)">{MARK_BODY}</g>
</svg>"""


def render(svg_text: str, px: int) -> Image.Image:
    png = subprocess.run(
        ["rsvg-convert", "-w", str(px), "-h", str(px)],
        input=svg_text.encode(),
        capture_output=True,
        check=True,
    ).stdout
    return Image.open(io.BytesIO(png)).convert("RGBA")


def save(img: Image.Image, path: pathlib.Path, opaque: bool = False) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    (img.convert("RGB") if opaque else img).save(path)
    print("wrote", path.relative_to(ROOT))


def main() -> None:
    # Full-bleed square (iOS, maskable web, Play listing): the platform masks the corners.
    square = svg("square", inset=0, mark_scale=1.2)
    # Rounded tile with margin (macOS, Windows, Linux, favicon): the shape is part of the icon.
    tile = svg("tile", inset=10, mark_scale=1.05)

    (HERE / "revera-icon.svg").write_text(square)
    (HERE / "revera-icon-tile.svg").write_text(tile)

    # iOS / iPadOS: a single 1024px, opaque, full-bleed icon (the OS applies the mask).
    save(render(square, 1024), ROOT / "apps/ios/Revera/Assets.xcassets/AppIcon.appiconset/AppIcon-1024.png", opaque=True)

    # macOS .icns
    with tempfile.TemporaryDirectory() as tmp:
        iconset = pathlib.Path(tmp) / "revera.iconset"
        iconset.mkdir()
        for base in (16, 32, 128, 256, 512):
            render(tile, base).save(iconset / f"icon_{base}x{base}.png")
            render(tile, base * 2).save(iconset / f"icon_{base}x{base}@2x.png")
        out = ROOT / "apps/desktop/icons/revera.icns"
        out.parent.mkdir(parents=True, exist_ok=True)
        if shutil.which("iconutil"):
            subprocess.run(["iconutil", "-c", "icns", str(iconset), "-o", str(out)], check=True)
            print("wrote", out.relative_to(ROOT))

    # Windows .ico and Linux .png
    big = render(tile, 256)
    big.save(ROOT / "apps/desktop/icons/revera.ico", sizes=[(s, s) for s in (16, 24, 32, 48, 64, 128, 256)])
    print("wrote apps/desktop/icons/revera.ico")
    save(render(tile, 512), ROOT / "apps/desktop/icons/revera.png")

    # Desktop window/taskbar icon at runtime (Compose resource)
    save(render(tile, 256), ROOT / "apps/desktop/src/main/composeResources/drawable/revera_icon.png")

    # Web: favicon + PWA icons (maskable = full-bleed)
    web = ROOT / "apps/web/src/wasmJsMain/resources"
    web.mkdir(parents=True, exist_ok=True)
    (web / "favicon.svg").write_text(tile)
    print("wrote", (web / "favicon.svg").relative_to(ROOT))
    save(render(tile, 192), web / "icons/icon-192.png")
    save(render(tile, 512), web / "icons/icon-512.png")
    save(render(square, 512), web / "icons/icon-maskable-512.png", opaque=True)
    save(render(tile, 180), web / "icons/apple-touch-icon.png")

    # Play Store listing icon (512px, opaque square; Play applies its own mask)
    save(render(square, 512), ROOT / "apps/android/src/main/ic_launcher-playstore.png", opaque=True)


if __name__ == "__main__":
    main()
