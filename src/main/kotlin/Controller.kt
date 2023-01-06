class Controller(var model: Model) {

    fun initAlternating() {
        model.initAlternating()
    }

    fun switchDataSet(index: Int) {
        model.switchDataSet(index)
    }

    fun switchVisualization(index: Int) {
        model.switchVisualization(index)
    }

    fun addEntry() {
        model.addEntry()
    }

    fun removeEntry(index: Int) {
        model.removeEntry(index)
    }

    fun createDataSet(dataSetName: String) {
        model.createDataSet(dataSetName)
    }

    fun changeEntryValue(index: Int, newValue: Double) {
        model.changeEntryValue(index, newValue)
    }
}
