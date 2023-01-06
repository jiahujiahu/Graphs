import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment

class DataEntryView(model: Model, controller: Controller) : ScrollPane(), IView {

    private val vbox = VBox()
    private val addEntryButton = Button("Add Entry")
    private val modelCopy = model
    private val controllerCopy = controller

    // When notified by the model that things have changed,
    // update to display the new value
    override fun updateView() {
        vbox.children.clear()
        content = vbox.apply {
            children.add(Label("Dataset name: " + modelCopy.dataSets[modelCopy.currentDataSetIndex].dataSetEntries.first))
            for (i in 0 until modelCopy.dataSets[modelCopy.currentDataSetIndex].dataSetEntries.second.size) {
                children.add(
                    DataSetEntry(modelCopy, controllerCopy, i, modelCopy.dataSets[modelCopy.currentDataSetIndex].dataSetEntries.second[i], modelCopy.dataSets[modelCopy.currentDataSetIndex].dataSetEntries.second.size == 1)
                )
            }
            children.add(addEntryButton.apply {
                maxWidth = Double.MAX_VALUE
            })
            padding = Insets(10.0)
            spacing = 10.0
        }
    }

    init {

        if (model.dataSets.isEmpty()) {
            controller.initAlternating()
        }

        content = vbox.apply {
            children.add(Label("Dataset name: " + model.dataSets[model.currentDataSetIndex].dataSetEntries.first))
            for (i in 0 until model.dataSets[model.currentDataSetIndex].dataSetEntries.second.size) {
                children.add(
                    DataSetEntry(model, controller, i, model.dataSets[model.currentDataSetIndex].dataSetEntries.second[i], modelCopy.dataSets[modelCopy.currentDataSetIndex].dataSetEntries.second.size == 1)
                )
            }
            children.add(addEntryButton.apply {
                maxWidth = Double.MAX_VALUE
            })
            padding = Insets(10.0)
            spacing = 10.0
        }

        vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS

        addEntryButton.setOnAction {
            controller.addEntry()
        }

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}