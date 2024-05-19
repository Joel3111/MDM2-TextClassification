import csv

def clean_csv(input_file, output_file):
    with open(input_file, 'r', encoding='utf-8') as infile, open(output_file, 'w', encoding='utf-8', newline='') as outfile:
        reader = csv.reader(infile)
        writer = csv.writer(outfile)

        for row in reader:
            cleaned_row = [column.replace('\n', ' ').replace('\r', ' ') for column in row]
            writer.writerow(cleaned_row)

if __name__ == "__main__":
    input_train = 'C:\\Users\\joele\\Desktop\\MDM_Project2\\demo\\train_data.csv'
    output_train = 'C:\\Users\\joele\\Desktop\\MDM_Project2\\demo\\cleaned_train_data.csv'
    input_test = 'C:\\Users\\joele\\Desktop\\MDM_Project2\\demo\\test_data.csv'
    output_test = 'C:\\Users\\joele\\Desktop\\MDM_Project2\\demo\\cleaned_test_data.csv'

    clean_csv(input_train, output_train)
    clean_csv(input_test, output_test)

    print("CSV files cleaned successfully.")
