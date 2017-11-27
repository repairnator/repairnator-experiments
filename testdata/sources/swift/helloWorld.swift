

    print("Hello, world!")


let banner = """
          __,
         (          o   /) _/_
          `.  , , , ,  //  /
        (___)(_(_/_(_ //_ (__
                     /)
                    (/
        """



    func greet(person: String, day: String) -> String {
        return "Hello \(person), today is \(day)."
    }
    greet(person: "Bob", day: "Tuesday")


    class NamedShape {
        var numberOfSides: Int = 0
        var name: String
        
        init(name: String) {
            self.name = name
        }
        
        func simpleDescription() -> String {
            return "A shape with \(numberOfSides) sides."
        }
    }

    class Square: NamedShape {
        var sideLength: Double
        
        init(sideLength: Double, name: String) {
            self.sideLength = sideLength
            super.init(name: name)
            numberOfSides = 4
        }
        
        func area() -> Double {
            return sideLength * sideLength
        }
        
        override func simpleDescription() -> String {
            return "A square with sides of length \(sideLength)."
        }
    }
    let test = Square(sideLength: 5.2, name: "my test square")
    test.area()
    test.simpleDescription()


