import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class VisualizationView(val model: Model, controller: Controller) : VBox(), IView {

    val canvas = Canvas(640.0*0.78, 480.0)
    val entries = model.dataSets[model.currentDataSetIndex].dataSetEntries

    var negativeEntries = false
    var minEntryValue = Double.MAX_VALUE
    var maxEntryValue = Double.MIN_VALUE
    var range = maxEntryValue - minEntryValue

    fun line() {
        if (entries.second.size == 1) {
            canvas.graphicsContext2D.apply {
                fill = Color.WHITE
                fillRect(0.0, 0.0, canvas.width, canvas.height)
                fill = Color.RED
                fillOval((canvas.width/2)-2.5, canvas.height-2.5, 5.0, 5.0)
            }
            return
        }
        var previousX = 0.0
        var previousY = canvas.height - (canvas.height * ((entries.second[0] - minEntryValue) / range))
        val widthX = canvas.width / (entries.second.size - 1)
        var currentX = 0.0
        var currentY = 0.0
        canvas.graphicsContext2D.apply {
            fill = Color.WHITE
            fillRect(0.0, 0.0, canvas.width, canvas.height)
            stroke = Color.BLACK
            lineWidth = 1.0
            fill = Color.RED
            fillOval(previousX-2.5, previousY-2.5, 5.0, 5.0)
        }
        for (i in 1 until entries.second.size) {
            canvas.graphicsContext2D.apply {
                currentX = previousX+widthX
                currentY = canvas.height - (canvas.height * ((entries.second[i] - minEntryValue) / range))
//                print(" x: $previousX ")
//                print(" y : $previousY ")
                strokeLine(previousX, previousY, currentX, currentY)
                fillOval(currentX-2.5, currentY-2.5, 5.0, 5.0)
                previousX = currentX
                previousY = currentY
            }
        }
    }

    fun bar(){
        val w = canvas.width
        val h = canvas.height
        val marginValue = 10.0
        range = max(max(maxEntryValue - minEntryValue, maxEntryValue), minEntryValue)
        var scale = 0.0
        if (range != 0.0) {
             scale = (h - 2*marginValue)/range
        }

        var hue = 0.0;
        val colorRatio = 360.0 / entries.second.size
        var previousX = marginValue
        val widthX= (w-2*marginValue)/((entries.second.size*2)-1)
        val zeroLine = h/2 + ((maxEntryValue + minEntryValue)/2 * scale)

        canvas.graphicsContext2D.clearRect(0.0,0.0,w,h)

        for (i in entries.second) {
            canvas.graphicsContext2D.apply {
                fill = Color.hsb(hue, 1.0, 1.0)
                fillRect(previousX, if ( i < 0 ) { zeroLine } else { zeroLine - i*scale } , widthX, abs(i*scale))
                // save()
            }
            previousX += 2 * widthX
            hue += colorRatio
        }

        canvas.graphicsContext2D.strokeLine(marginValue, zeroLine,w-marginValue,zeroLine)
    }
    fun barSEM(){
        val w = canvas.width
        val h = canvas.height
        range = max(max(maxEntryValue - minEntryValue, maxEntryValue), minEntryValue)

        val marginValue = 10.0
        var scale = 0.0
        if (range != 0.0) {
            scale = (h - 2*marginValue)/range
        }

        var hue = 0.0;
        var previousX = 5.0
        var widthX = (w-2*marginValue)/((entries.second.size*2)-1)
        var zeroLine = h/2 + (maxEntryValue+minEntryValue)/2 * scale
        var sd = 0.0
        var sum = 0.0

        canvas.graphicsContext2D.clearRect(0.0,0.0, w, h)

        for (i in entries.second){
            sum = sum + i
        }

        val mean = sum/entries.second.size

        for(i in entries.second){
            sd = sd + Math.pow(i-mean,2.0)
        }
        sd = sqrt(sd/(entries.second.size-1))

        var sqrtSampleSize = sqrt(entries.second.size.toDouble())
        val SEM = sd/ sqrtSampleSize

        var colorRatio = 360.0/entries.second.size
        for (i in entries.second) {
            canvas.graphicsContext2D.apply {
                fill = Color.hsb(hue, 1.0, 1.0)
                fillRect(previousX, if ( i < 0 ) { zeroLine } else { zeroLine - i*scale } , widthX, abs(i*scale))
            }
            previousX += 2 * widthX
            hue += colorRatio
        }
        canvas.graphicsContext2D.apply {
            strokeText("mean: $mean",25.0,25.0,250.0) // displaying the current values for mean
            strokeText("SEM: $SEM",25.0,45.0,250.0) // displaying the current values for SEM
            strokeLine(marginValue, h-mean*scale,w-marginValue,h-mean*scale) // indicating the mean of the data set
            save()
            setLineDashes(10.0)
            strokeLine(marginValue, h-(mean+SEM)*scale,w-marginValue,h-(mean+SEM)*scale) // indicating the area of + sd of mean
            strokeLine(marginValue, h-(mean-SEM)*scale,w-marginValue,h-(mean-SEM)*scale) // indicating the area of - sd of mean
            restore()
            strokeLine(marginValue, zeroLine,w-marginValue,zeroLine) // indicating the value 0.0
        }
    }
    fun pie(){
        val w = canvas.width
        val h = canvas.height

        canvas.graphicsContext2D.clearRect(0.0,0.0,w,h)

        var previousAngle = 0.0
        val marginValue = 10.0
        var xPosition = marginValue
        var yPosition = marginValue
        var diameter = 0.0
        if ( w<h ) {
            diameter = w - 2 * marginValue
            yPosition = (h-diameter)/2
        } else {
            diameter = h - 2 * marginValue
            xPosition = (w-diameter)/2
        }
        var hue = 0.0;
        var colorRatio = 360.0/ entries.second.size

        var sum = 0.0
        for (i in entries.second) {
            sum += i
        }

        var scale = 360.0 / sum

        for (j in entries.second) {
            canvas.graphicsContext2D.apply {
                fill = Color.hsb(hue, 1.0, 1.0)
                fillArc(xPosition, yPosition, diameter, diameter, previousAngle, j*scale, ArcType.ROUND)
            }
            previousAngle = previousAngle + j*scale
            hue += colorRatio
        }
    }

    // When notified by the model that things have changed,
    // update to display the new value
    override fun updateView() {
        negativeEntries = false
        minEntryValue = Double.MAX_VALUE
        maxEntryValue = Double.MIN_VALUE
        for (i in model.dataSets[model.currentDataSetIndex].dataSetEntries.second) {
            if (i < 0) {
                negativeEntries = true
            }
            if (i < minEntryValue) {
                minEntryValue = i
            }
            if (i > maxEntryValue) {
                maxEntryValue = i
            }
        }
        range = if (maxEntryValue == minEntryValue) { minEntryValue * 4 } else { maxEntryValue - minEntryValue }

        if (model.currentVisualizationIndex == 0) { // Line
            line()
        } else if (model.currentVisualizationIndex == 1) { // Bar
            bar()
        } else if (model.currentVisualizationIndex == 2) { // Bar (SEM)
            if(!negativeEntries){
                barSEM()
            }
        } else if (model.currentVisualizationIndex == 3) { // Pie
            if(!negativeEntries){
                pie()
            }
        }
    }

    init {

        // add button widget to the pane
        children.add(canvas)

//        padding = Insets(10.0)
//        background = Background(
//            BackgroundFill(Color.WHITE, CornerRadii(0.0), Insets(0.0))
//        )

        // register with the model when we're ready to start receiving data
        model.addView(this)
    }
}