package repo

import com.mongodb.DBObject
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import domain.{WordEntry, WordEntryProperties}
import play.Logger

/**
 * Created by p.samaddar on 02/05/2015.
 */
class WordRepo {


  val mongoClient =  MongoClient()
  val db = mongoClient("test")

  def words:List[WordEntry] = {
    val words = db("words")

    Logger.debug("Retrived "+words.size+ " words from mongoDB")

    words map WordEntryFromMongoObject toList

  }

  def convertWordToMongoObject(word:WordEntry):DBObject ={
    MongoDBObject(
      WordEntryProperties.WORD -> word.word.toLowerCase,
      WordEntryProperties.MEANING -> word.meaning,
      WordEntryProperties.BOOK ->word.book
    )
  }

  def WordEntryFromMongoObject(dBObject: DBObject)={
    WordEntry(
      dBObject.get(WordEntryProperties.WORD).toString.capitalize,
      dBObject.get(WordEntryProperties.MEANING).toString,
      dBObject.get(WordEntryProperties.BOOK).toString)
  }

  def saveWord(word:WordEntry)={
    Logger.debug("Will try to save :"+word)
    val words = db("words")

    words.insert(convertWordToMongoObject(word))
  }

  def search(wordToSearch: String):List[WordEntry] = {
    val words = db("words")

    val queryWord = (".*"+wordToSearch+".*").r
    val queryBuilder = MongoDBObject(WordEntryProperties.WORD -> queryWord)

    words.find(queryBuilder) map WordEntryFromMongoObject toList
  }

}
