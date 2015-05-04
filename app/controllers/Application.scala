package controllers

import domain.WordEntry
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
      "word" -> nonEmptyText,
      "meaning" -> nonEmptyText,
      "book" -> nonEmptyText
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

}