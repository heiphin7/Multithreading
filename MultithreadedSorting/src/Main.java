/*
* Данный код реализует многопоточную сортировку в Java
* у нас есть 2 метода сортировки: QuickSort и MergeSort
* Далее, мы создаем потоки и заготовленный массив чисел array
* Затем, мы запускаем их по очереди, сначала quickSort и mergeSort
*
* Мы можем запустить 2 потока сразу, это привидёт к "гонке" потоков
* и это будет ярким примером многопоточности
*
*/



public class Main {
    public static void main(String[] args) {
        // Наш исходный массив для сортровки
        int[] array = new int[]{42, 2, 63, 43, 12, 22, 13, 26, 52, 70};

        // Инициализация потоков
        Thread quickSort = new Thread(new QuickSort(array));
        Thread mergesort = new Thread(new MergeSort(array));

        // Запуск потоков
        quickSort.start();
        // Для того, чтобы запустить 2 потока одновременно (для демонстрации многопоточности)
        // Нужно просто закомментить блок try - catch
        try {
            // join() это нужно для ожидания полного завершения quicksort потока
            quickSort.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        // Запуск второго потока
        mergesort.start();
    }

    // Это класс, который будет потоком, для этого он должен
    // наследовать интерфейс Runnable, который содержит всег 1 метод - run()
    static class QuickSort implements Runnable{

        public int[] array;
        // Конструктор класса, с помощью которого можем передавать в него массив
        public QuickSort(int[] array){
            this.array = array;
        }

        // Реализация сортировки
        public static void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pivot = partition(arr, low, high);
                quickSort(arr, low, pivot - 1);
                quickSort(arr, pivot + 1, high);
            }
        }

        public static int partition(int[] arr, int low, int high) {
            int pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            return i + 1;
        }

        // Метод run(), который служит для запуска потока(Это как метод main() используется для запуска программы)
        @Override
        public void run() {
            quickSort(array, 0, array.length - 1);
            System.out.print("Сортировка QuickSort закончила свою работу, результат: ");
            for (int i: array) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    // Это точно такой же класс как quicksort, только он использует другой алгоритм сортировки
    static class MergeSort implements Runnable{
        public int[] array;

        // Такой же конструктор для передачи в параметры массив
        public MergeSort(int[] array){
            this.array = array;
        }

        // Реализация уже другого алгоритма сортировки
        public static void mergeSort(int[] arr, int l, int r) {
            if (l < r) {
                int m = (l + r) / 2;
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);
                merge(arr, l, m, r);
            }
        }

        public static void merge(int[] arr, int l, int m, int r) {
            int n1 = m - l + 1;
            int n2 = r - m;
            int[] L = new int[n1];
            int[] R = new int[n2];
            for (int i = 0; i < n1; i++)
                L[i] = arr[l + i];
            for (int j = 0; j < n2; j++)
                R[j] = arr[m + 1 + j];
            int i = 0, j = 0;
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }

        // Реализация метода run() для запуска потока
        @Override
        public void run() {
            mergeSort(array, 0, array.length - 1);

            System.out.print("Сортировка mergerSort закончила свою работу, результат: ");
            for (int i: array) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}