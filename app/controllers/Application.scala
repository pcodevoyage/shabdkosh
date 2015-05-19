package controllers

import domain.{SearchWordProperties, WordEntryProperties, SearchWord, WordEntry}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.mvc._
import repo.WordRepo

object Application extends Controller {

  private val repo = new WordRepo

  def index = Action {
    Ok(views.html.index("Your new applicatsdfdsfd ion is ready."))
  }

  def listOfWords = Action{
    Ok(views.html.wordList(repo.words))
  }

  private val wordForm:Form[WordEntry] = Form(
    mapping(
      WordEntryProperties.WORD -> nonEmptyText,
      WordEntryProperties.MEANING -> nonEmptyText,
      WordEntryProperties.BOOK -> nonEmptyText
    )(WordEntry.apply)(WordEntry.unapply)
  )

  def save = Action { implicit request =>
    val form = wordForm.bindFromRequest()

    form.fold(
      hasErrors = { form =>
        Redirect(routes.Application.listOfWords)
      },
      success = { form =>
        repo.saveWord(form)
        Redirect(routes.Application.listOfWords)
      }
    )
  }

  def newWord = Action { implicit  request =>
    Ok(views.html.addWord(wordForm))
  }

  def search = Action { implicit request =>
    val form = searchFrom.bindFromRequest()

    form.fold(
      hasErrors = { form =>
        Redirect(routes.Application.newSearch)

      },
      success = {form =>
        val f =repo.search(form.wordToSearch)
        Ok(views.html.wordList(f))
      }
    )

  }

  private val searchFrom:Form[SearchWord] = Form(
    mapping (
      SearchWordProperties.WORD -> nonEmptyText
    )(SearchWord.apply)(SearchWord.unapply)
  )

  def newSearch = Action {
    Ok(views.html.search(searchFrom))
  }

}