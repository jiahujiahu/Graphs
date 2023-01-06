import javafx.collections.FXCollections.observableArrayList
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javax.tools.Tool

class ToolbarView(val model: Model, controller: Controller) : HBox(), IView {

    private var dataSetList = observableArrayList("alternating")
    private var choiceBox = ChoiceBox(dataSetList).apply {
        selectionModel.select(0)
        prefWidth = 140.0
    }

    private var separator1 = Separator().apply {
        orientation = Orientation.VERTICAL
        padding = Insets(0.0, 10.0, 0.0, 10.0)
    }

    // private var newDataSetName = ""
    private var textField = TextField().apply {
        promptText = "Data set name"
    }
    private var createButton = Button("Create")
    private var separator2 = Separator().apply {
        orientation = Orientation.VERTICAL
        padding = Insets(0.0, 10.0, 0.0, 10.0)
    }

    private var button0 = Button("Line").apply {
        prefWidth = 70.0
    }
    private var button1 = Button("Bar").apply {
        prefWidth = 70.0
    }
    private var button2 = Button("Bar(SEM)").apply {
        prefWidth = 70.0
    }
    private var button3 = Button("Pie").apply {
        prefWidth = 70.0
    }

    // When notified by the model that things have changed,
    // update to display the new value
    override fun updateView() {}

    init {

        // data set selector
        children.add(choiceBox)
        children.add(separator1)

        choiceBox.selectionModel.selectedIndexProperty().addListener{_, _, newIndex ->
            controller.switchDataSet(newIndex as Int)
        }

        // data set creator
        children.add(textField)
        children.add(createButton)
        children.add(separator2)

//        textField.textProperty().addListener{ _, _, newValue ->
//            newDataSetName = newValue
//        }

        createButton.setOnAction {
//            controller.createDataSet(newDataSetName)
//            dataSetList.add(newDataSetName)
//            choiceBox.selectionModel.select(dataSetList.size-1)
//            newDataSetName = ""
//            textField.text = ""
            controller.createDataSet(textField.text)
            dataSetList.add(textField.text)
            choiceBox.selectionModel.select(dataSetList.size-1)
            textField.text = ""
        }

        // visualization selector
        children.add(button0)
        children.add(button1)
        children.add(button2)
        children.add(button3)

        button0.setOnAction {
            controller.switchVisualization(0)
        }
        button1.setOnAction {
            controller.switchVisualization(1)
        }
        button2.setOnAction {
            controller.switchVisualization(2)
        }
        button3.setOnAction {
            controller.switchVisualization(3)
        }

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}
