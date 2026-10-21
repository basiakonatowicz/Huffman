# AiSD2025ZEx5: Kompresor Huffmana
## 1. Kompilacja i Budowanie Projektu

Aby skompilować kod i stworzyć uruchamialny plik JAR, należy wykonać następujące kroki w katalogu głównym projektu. (Bash):

mvn clean package

uruchamialny plik JAR (AiSD2025ZEx5.jar) zostanie umieszczony w katalogu target/

## 2. Uruchomienie Programu

Program przyjmuje argumenty w postaci flag. 
Struktura Polecenia Bash

java -jar target/AiSD2025ZEx5.jar -m [tryb] -s [plik_źródłowy] -d [plik_docelowy] -l [długość_L]

Flaga,Opis,Wymagane dla
-m,Tryb pracy (comp lub decomp).,Wszystkich
-s,Ścieżka do pliku źródłowego.,Wszystkich
-d,"Ścieżka, gdzie ma zostać zapisany plik wynikowy.",Wszystkich
-l,Długość bloku L.,comp

## 3. Przykładowa Działająca Instrukcja (Bash)

A. Kompresja (Tryb comp)

java -jar target/AiSD2025ZEx5.jar -m comp -s input.txt -d compressed.huff -l 3

B. Dekompresja (Tryb decomp)

java -jar target/AiSD2025ZEx5.jar -m decomp -s compressed.huff -d restored.txt 

## 5. Weryfikacja

Aby potwierdzić bezstratność kompresji, należy porównać plik źródłowy (input.txt) z plikiem odtworzonym (restored.txt):

cmp input.txt restored.txt

Komenda cmp na systemach Linux/macOS nie zwraca komunikatu, jeśli pliki są identyczne.
