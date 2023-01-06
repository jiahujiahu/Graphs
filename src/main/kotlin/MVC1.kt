import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

// MVC1 Separate View and Controller (not typically done, but example of "pure" MVC)
// A simple MVC example inspired by Joseph Mack, http://www.austintek.com/mvc/
// This version uses MVC: two views coordinated with the observer pattern and a separate controller.
class MVC1 : Application() {
    override fun start(stage: Stage) {
        // window name
        stage.title = this.javaClass.name

        // create and initialize the Model to hold our counter
        val model = Model()

        // create the Controller, and tell it about Model
        // the controller will handle input and pass requests to the model
        val controller = Controller(model)

        // create each view, and tell them about model and controller
        // the views will register themselves with these components
        // and handle displaying the data from the model
        val toolbarView = ToolbarView(model, controller)
        val dataEntryView = DataEntryView(model, controller)
        val visualizationView = VisualizationView(model, controller)

        val root = BorderPane()
        root.top = toolbarView
        root.left = dataEntryView
        root.center = visualizationView

        // setup and show the stage (window)
        stage.apply {
            title = "CS349 - A2 Graphs - j349hu"
            scene = Scene(root, 800.0, 600.0)
            minWidth = 640.0
            minHeight = 480.0
        }.show()
    }
}