package org.kotlinacademy.views

import kotlinx.html.FORM
import kotlinx.html.InputType.number
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.kotlinacademy.common.getUrlParam
import org.kotlinacademy.data.Feedback
import org.kotlinacademy.data.InfoData
import org.kotlinacademy.data.PuzzlerData
import react.RBuilder
import react.ReactElement
import react.dom.*
import kotlin.browser.document
import kotlin.js.Math

fun RBuilder.feedbackFormView(id: Int?, onSubmit: (Feedback) -> Unit): ReactElement? = kaForm {
    val general = id == null
    h3 { if (general) +"General comment" else +"Article comment" }

    val ratingField = numberFieldView("Please, rate using a number between 0 and 10")

    val whatIsCommentAbout = if (general) "Kotlin Academy" else "this article"
    val commentField = textFieldView("What do you think about $whatIsCommentAbout?")

    val suggestionsField = textFieldView("Can you give us some advice how to make it better?")

    submitButton("Send", onClick = fun() {
        val feedback = Feedback(id ?: -1,
                rating = ratingField.value ?: return,
                comment = commentField.value ?: "",
                suggestions = suggestionsField.value ?: ""
        )
        onSubmit(feedback)
    })
}

fun RBuilder.infoFormView(onSubmit: (InfoData) -> Unit): ReactElement? = kaForm {
    h3 { +"Share important news from last weeks" }

    val titleField = textFieldView("Title of the news", name = "title", lines = 1)
    val imageField = textFieldView("Url to image", name = "image-url", lines = 1)
    val descriptionField = textFieldView("Here you can describe the news", name = "description")
    val sourcesField = textFieldView("Give some sources for us and readers", name = "sources")
    val urlField = textFieldView("Does this news refer to some URL? If so, leave it here.", name = "url", lines = 1)
    val authorField = textFieldView("Your name", name = "author", lines = 1)
    val authorUrlField = textFieldView("Your url", name = "author-url", lines = 1)

    submitButton("Submit", onClick = fun() {
        val info = InfoData(
                title = titleField.value ?: return,
                imageUrl = imageField.value ?: return,
                description = descriptionField.value ?: return,
                sources = sourcesField.value ?: "",
                url = urlField.value,
                author = authorField.value,
                authorUrl = authorUrlField.value
        )
        onSubmit(info)
    })
}

fun RBuilder.puzzlerFormView(onSubmit: (PuzzlerData) -> Unit): ReactElement? = kaForm {
    h3 { +"Share your puzzler :D" }

    val titleField = textFieldView("Title", name = "title", lines = 1)
    val questionField = textFieldView("Question", name = "question")
    val answersField = textFieldView("Give some possible answers", name = "answer")
    val authorField = textFieldView("Your name", name = "author", lines = 1)
    val authorUrlField = textFieldView("Your url", name = "author-url", lines = 1)

    submitButton("Submit", onClick = fun() {
        val info = PuzzlerData(
                title = titleField.value ?: return,
                question = questionField.value ?: return,
                answers = answersField.value ?: return,
                author = authorField.value,
                authorUrl = authorUrlField.value
        )
        onSubmit(info)
    })
}

private fun RBuilder.kaForm(builder: RDOMBuilder<FORM>.() -> Unit) = div(classes = "list-center") {
    form(classes = "center-text") {
        builder()
    }
}

private fun RDOMBuilder<FORM>.numberFieldView(text: String, name: String? = null): FormFieldNumber {
    val inputId: String = name ?: randomId()
    div(classes = "mdc-text-field mdc-text-field--textarea") {
        input(classes = "mdc-text-field__input", type = number) {
            setProp("placeholder", text)
            attrs {
                id = inputId
                if (name != null) defaultValue = getUrlParam(name) ?: ""
            }
        }
    }
    return FormFieldNumber(inputId)
}

private fun RDOMBuilder<FORM>.textFieldView(text: String, name: String? = null, lines: Int = 8): FormFieldText {
    val inputId: String = name ?: randomId()
    div(classes = "mdc-text-field mdc-text-field--textarea") {
        textArea(classes = "mdc-text-field__input input-text-comment") {
            setProp("type", "text")
            setProp("cols", "40")
            setProp("rows", lines.toString())
            setProp("placeholder", text)
            attrs {
                id = inputId
                if (name != null) defaultValue = getUrlParam(name) ?: ""
            }
        }
    }
    return FormFieldText(inputId)
}

private class FormFieldText(private val id: String) {
    val value: String? get() = valueOn(id)
}

private class FormFieldNumber(private val id: String) {
    val value: Int? get() = valueOn(id)
}

fun <T> valueOn(elementId: String): T? {
    val text = document.getElementById(elementId).asDynamic().value as String
    return text.takeUnless { it.isBlank() } as T
}

private fun RDOMBuilder<FORM>.submitButton(text: String, onClick: () -> Unit) {
    button(classes = "mdc-button mdc-button--raised") {
        attrs {
            onClickFunction = { onClick() }
        }
        +text
    }
}

fun randomId(): String {
    val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..10).map { possible[Math.floor(possible.length * Math.random())] }.fold("", { acc, c -> acc + c })
}