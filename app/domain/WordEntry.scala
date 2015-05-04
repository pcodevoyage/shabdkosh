package domain

case class WordEntry(word:String,meaning:String,book:String)

object WordEntryProperties{
  val WORD = "word"
  val MEANING = "meaning"
  val BOOK = "book"
}
