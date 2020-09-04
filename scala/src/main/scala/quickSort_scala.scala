object quickSort_scala {

  def quick_sort(list: List[Int], begin: Int, end: Int): List[Int]={
    if (begin > end){

    }
    val left = 0
    val right: Int = list.length - 1
    val pivot: Int = partition(list, left, right)
    quick_sort(list, left, pivot)
    quick_sort(list, pivot, right)
  }



  def partition(list: List[Int], begin: Int, end: Int): Int={
    begin
  }

}
