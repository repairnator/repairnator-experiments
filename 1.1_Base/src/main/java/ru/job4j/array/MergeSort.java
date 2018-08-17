package ru.job4j.array;
/**
*Класс выполняющий сортировку слиянием.
*@author ifedorenko
*@since 23.08.2017
*@version 1
*/

public class MergeSort {
	/**
	*Основной метод в программе. В методе из двух отсортированных массивов создается один отсортированный массив.
	*@param a1 Входящий параметр. Первый отсортированный массив чисел.
	*@param a2 Входящий параметр. Второй отсортированный массив чисел.
	*@return a3 Отсортированный массив, состоящий из двух переданных в метод массивов.
	*/
	public int[] sort(int[] a1, int[] a2) {
		int[] a3 = new int[a1.length + a2.length];
		int i = 0; //индекс элементов для первого массива
		int j = 0; //индекс элементов для второго массива
		for (int k = 0; k < a3.length; k++) {
			if (i == a1.length) {  // Проверяем, все ли элементы первого массива мы проверили
				a3[k] = a2[j];
				j++;
			} else if (j == a2.length) { // Проверяем все ли элементы второго массива мы проверили
				a3[k] = a1[i];
				i++;
			} else if (a1[i] <= a2[j]) {
				a3[k] = a1[i];
				i++;
			} else if (a1[i] > a2[j]) {
				a3[k] = a2[j];
				j++;
			}
		}
		return a3;
	}
}
