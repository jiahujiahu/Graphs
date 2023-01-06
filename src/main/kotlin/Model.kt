class Model {

    //region View Management

    // all views of this model
    private val views: ArrayList<IView> = ArrayList()

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    fun addView(view: IView) {
        views.add(view)
        view.updateView()
    }

    fun removeView(view: IView?) {
        // remove view here
    }

    // the model uses this method to notify all of the Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private fun notifyObservers() {
        for (view in views) {
            println("Model: notify View")
            view.updateView()
        }
    }

    private fun notifyCenter() {
        views[2].updateView()
    }

    //endregion

    var currentDataSetIndex = 0
    var currentDataSetName = ""
    var currentVisualizationIndex = 0
    var dataSets = ArrayList<DataSetEntries>()

    fun initAlternating() {
        var arrayList = ArrayList<Double>().apply {
            add(-1.0)
            add(3.0)
            add(-1.0)
            add(3.0)
            add(-1.0)
            add(3.0)
        }
        var firstDataSet = (DataSetEntries("alternating", arrayList))
        currentDataSetName = "alternating"
        dataSets.add(firstDataSet)
        notifyObservers()
    }

    fun switchDataSet(index: Int) {
        currentDataSetIndex = index
        currentDataSetName = dataSets[currentDataSetIndex].dataSetEntries.first
        notifyObservers()
    }

    fun switchVisualization(index: Int) {
        currentVisualizationIndex = index
        notifyCenter()
    }

    fun addEntry() {
        dataSets[currentDataSetIndex].dataSetEntries.second.add(0.0)
        notifyObservers()
    }

    fun removeEntry(index: Int) {
        if (dataSets[currentDataSetIndex].dataSetEntries.second.size != 1) {
            dataSets[currentDataSetIndex].dataSetEntries.second.removeAt(index)
            notifyObservers()
        }
    }

    fun createDataSet(dataSetName: String) {
        currentDataSetName = dataSetName
        currentDataSetIndex = dataSets.size
        var arrayList = ArrayList<Double>()
        arrayList.add(0.0)
        dataSets.add(DataSetEntries(currentDataSetName, arrayList))
        notifyObservers()
    }

    fun changeEntryValue(index: Int, newValue: Double) {
        dataSets[currentDataSetIndex].dataSetEntries.second[index] = newValue
        notifyCenter()
    }
}
