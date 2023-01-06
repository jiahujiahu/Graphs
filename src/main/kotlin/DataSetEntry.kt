import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.w3c.dom.Text

class DataSetEntry(model: Model, controller: Controller, index: Int, value: Double, disable: Boolean) : HBox() {
    private val label = Label("Entry #$index")
    private val textField = TextField(value.toString())
    private val removeButton = Button("X").apply {
        isDisable = disable
    }

    init {
        children.add(label)
        children.add(textField).apply {
            maxWidth = Double.MAX_VALUE
        }
        children.add(removeButton)
        spacing = 10.0

        textField.textProperty().addListener { _, _, newValue ->
            if (newValue.matches(Regex("(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?"))) {
                controller.changeEntryValue(index, newValue.toDouble())
            }
        }
        removeButton.setOnAction {
            controller.removeEntry(index)
        }
    }
}
