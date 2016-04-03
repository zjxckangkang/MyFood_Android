package com.lvkang.app.myfoodandroid.myfood.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SortArrayList {
//	public static void main(String[] args) {
//		String keyString = "A";
//		int offset = 1200;
//		ArrayList<HashMap<String, String>> list = getRandomArrayList(keyString,
//				offset);
//		ArrayList<HashMap<String, String>> list2 = getRandomArrayList(
//				keyString, offset);
//
////		System.out.println(list.toString());
//
////		long time = new Date().getTime();
////		list2 = getSortedArrayListByBunble(keyString, list);
////		System.out.println(new Date().getTime() - time + " test1");
//
//		ArrayList<HashMap<String, String>> list3 = getRandomArrayList(
//				keyString, offset);
//		long time2 = new Date().getTime();
//		list3 = getSortedArrayListByQuick(keyString, list);
//		 System.out.println(list3);
//		System.out.println(offset + "  " + (new Date().getTime() - time2)
//				+ " test2");
//	}

	// private static class MyRunnable implements Runnable {
	// String keyString;
	// int offset;
	//
	// MyRunnable(String keyString, int offset) {
	// this.keyString = keyString;
	// this.offset = offset;
	// }
	// @Override
	// public void run() {
	//

	// }
	//
	// }

	private static ArrayList<HashMap<String, String>> getRandomArrayList(
			String key, int offset) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < offset; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(key, new Random().nextInt(1000) + "");
			list.add(map);
		}

		return list;
	}

//	private static ArrayList<HashMap<String, String>> getSortedArrayListByBunble(
//			String key, ArrayList<HashMap<String, String>> list) {
//		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
//		int offset = list.size();
//		int[] number = new int[offset];
//		int[] index = new int[offset];
//		for (int i = 0; i < offset; i++) {
//			number[i] = Integer.parseInt(list.get(i).get(key));
//			index[i] = i;
//
//		}
//		for (int i = 0; i < offset - 1; i++) {
//
//			for (int j = i + 1; j < offset; j++) {
//				if (number[i] > number[j]) {
//					int temp = number[j];
//					number[j] = number[i];
//					number[i] = temp;
//
//					int temp2 = index[j];
//					index[j] = index[i];
//					index[i] = temp2;
//
//				}
//
//			}
//
//		}
//		for (int i = 0; i < offset; i++) {
//			result.add(list.get(index[i]));
//		}
//
//		return result;
//	}

	public static ArrayList<HashMap<String, Object>> getSortedArrayListByQuick(
			String key, ArrayList<HashMap<String, Object>> foodMenuArrayList,boolean DESC) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		int offset = foodMenuArrayList.size();
		int[] number = new int[offset];
		int[] index = new int[offset];
		for (int i = 0; i < offset; i++) {
			number[i] = Integer.parseInt(foodMenuArrayList.get(i).get(key).toString());
			index[i] = i;

		}
		quickSort(index, number, 0, offset - 1);
		
		if (DESC) {
			for (int i = offset-1; i >-1; i--) {
				result.add(foodMenuArrayList.get(index[i]));
			}
		}else {
			
			for (int i = 0; i < offset; i++) {
				result.add(foodMenuArrayList.get(index[i]));
			}
		}
		return result;
	}

	private static void quickSort(int[] index, int[] n, int left, int right) {
		int pivot;
		if (left < right) {
			// pivot作为枢轴，较之小的元素在左，较之大的元素在右
			pivot = partition(index, n, left, right);
			// 对左右数组递归调用快速排序，直到顺序完全正确
			quickSort(index, n, left, pivot - 1);
			quickSort(index, n, pivot + 1, right);
		}
	}

	private static int partition(int[] index, int[] n, int left, int right) {
		int pivotkey = n[left];
		int pivotkey2 = index[left];
		// 枢轴选定后永远不变，最终在中间，前小后大
		while (left < right) {
			while (left < right && n[right] >= pivotkey)
				--right;
			// 将比枢轴小的元素移到低端，此时right位相当于空，等待低位比pivotkey大的数补上
			n[left] = n[right];
			index[left] = index[right];
			while (left < right && n[left] <= pivotkey)
				++left;
			// 将比枢轴大的元素移到高端，此时left位相当于空，等待高位比pivotkey小的数补上
			n[right] = n[left];
			index[right] = index[left];

		}
		// 当left == right，完成一趟快速排序，此时left位相当于空，等待pivotkey补上
		n[left] = pivotkey;
		index[left] = pivotkey2;
		return left;
	}
}
