import csv

# Funktion zum Entfernen von Anführungszeichen und Kommas im Text
def clean_text(input_text):
    return input_text.replace('"', '').replace(',', '')

# Lesen der CSV-Datei und Bearbeiten des Textes
def process_csv(input_file, output_file):
    with open(input_file, mode='r', encoding='utf-8') as infile, open(output_file, mode='w', encoding='utf-8', newline='') as outfile:
        reader = csv.reader(infile)
        writer = csv.writer(outfile)
        
        # Lesen der Kategorien (erste Zeile)
        headers = next(reader)
        writer.writerow(headers)
        
        # Bearbeiten und Schreiben der Datensätze
        for row in reader:
            label = row[0]
            text = row[1]
            cleaned_text = clean_text(text)
            writer.writerow([label, cleaned_text])

# Dateipfade
input_files = [
    r'C:\Users\joele\Desktop\MDM_Project2\Data_test.csv',
    r'C:\Users\joele\Desktop\MDM_Project2\Data_Train.csv'
]
output_files = [
    r'C:\Users\joele\Desktop\MDM_Project2\Data_test_cleaned.csv',
    r'C:\Users\joele\Desktop\MDM_Project2\Data_Train_cleaned.csv'
]

# Verarbeitung starten
for input_file, output_file in zip(input_files, output_files):
    process_csv(input_file, output_file)
