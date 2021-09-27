/*
 merge sort
 */
fn merge(nums: &mut [i32], merged_nums:&mut [i32], left: usize, mid:  usize, right: usize) {
    let mut i = left;
    let mut j= mid + 1;
    let mut k = left;

    while i<= mid && j <= right {
        if nums[i] <= nums[j] {
            merged_nums[k]=nums[i];
            i=i+1;
        } else {
            merged_nums[k]=nums[j];
            j=j+1;
        }
        k=k+1;
    }
    while i<=mid {
        merged_nums[k] = nums[i];
        i = i + 1;
        k = k + 1;
    }
    while j<= right {
        merged_nums[k]=nums[j];
        j = j+1;
        k = k + 1;
    }
}

fn merge_pass(nums: &mut [i32], merged_nums:&mut [i32], len: usize) {
    let mut i =0;
    let size = nums.len();
    while i + 2 * len <= size {
        merge(nums, merged_nums, i, i+len-1, i+2*len -1);
        i= i+ 2*len;
    }
    // less than 2*len
    if i + len < size {
        merge(nums, merged_nums, i, i+len-1, size -1);
    } else {
        while i < size {
            merged_nums[i] = nums[i];
            i=i+1;
        }
    }
}

fn merge_sort(nums:&mut [i32], merged_nums:&mut [i32]) {
    let mut len  = 1;
    while len < nums.len() {
        merge_pass(nums, merged_nums, len);
        len = len * 2;
        merge_pass(merged_nums, nums, len);
        len = len * 2;
    }
}

fn testMergeSort() {
    let mut nums = [10, 1, 2, 3, 5, 6, 3, 7, 8, 10, 19, 4];
    let mut temp_nums = [0;12];

    merge_sort(&mut nums[..], &mut temp_nums[..]);

    for num in nums {
        print!( "{} ", num);
    }

}

fn testMergeSort2() {
    let mut nums = [10, 1, 2, 3, 5, 6, 3, 7, 8, 10, 19, 4, 1];
    let mut temp_nums = [0;13];

    merge_sort(&mut nums[..], &mut temp_nums[..]);

    for num in nums {
        print!( "{} ", num);
    }
}