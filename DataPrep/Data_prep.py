import os
import shutil
import random

def get_all_images(root_dir):
    """
    Rekursiv alle Bilddateien in einem Verzeichnis und seinen Unterverzeichnissen sammeln.
    """
    image_files = []
    for root, dirs, files in os.walk(root_dir):
        for file in files:
            if file.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp', '.gif')):
                image_files.append(os.path.join(root, file))
    return image_files

def copy_images(image_files, output_dir, sample_size=25000):
    """
    Zufällige Auswahl von Bildern kopieren und in das Ausgabe-Verzeichnis speichern.
    """
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    sampled_files = random.sample(image_files, sample_size)
    for file in sampled_files:
        relative_path = os.path.relpath(file, start=input_dir)
        dest_path = os.path.join(output_dir, relative_path)
        os.makedirs(os.path.dirname(dest_path), exist_ok=True)
        shutil.copy(file, dest_path)

# Pfade anpassen
input_dir = r'C:\Users\joele\Desktop\TestVorlesung\playground\ut-zap50k-images-square'
output_dir = r'C:\Users\joele\Desktop\TestVorlesung\playground\reduced_dataset'

# Alle Bilddateien sammeln
all_images = get_all_images(input_dir)

# Sicherstellen, dass wir mindestens 25.000 Bilder haben
if len(all_images) < 25000:
    raise ValueError("Nicht genügend Bilder im Eingabeverzeichnis")

# Bilder auswählen und kopieren
copy_images(all_images, output_dir, sample_size=25000)

print(f"25.000 Bilder wurden erfolgreich in das Verzeichnis '{output_dir}' kopiert.")
