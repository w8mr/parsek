
rootProject.name = "jafun"


include(":parsek")
project(":parsek").projectDir = file("../parsek")
include(":kasmine")
project(":kasmine").projectDir = file("../kasmine")
