type Iterable object {
    public function __iterator() returns abstract object {

        public function next() returns record {|int value;|}?;
    } {
        object {
            int[] integers = [12, 34, 56, 34, 78, 21, 90];
            int cursorIndex = 0;
            public function next() returns record {|int value;|}? {
                self.cursorIndex += 1;
                if (self.cursorIndex <= 7) {
                    return {
                        value: self.integers[self.cursorIndex - 1]
                    };
                } else {
                    return ();
                }
            }
        } sample = new;
        return sample;
    }
};

public function testIterableObject() returns int[] {
    Iterable p = new Iterable();
    int[] integers = from var item in p
                     select item;

    return integers;
}

type AnotherIterable object {
    public function __iterator() returns abstract object {

        public function next() returns record {|Iterable value;|}?;
    } {
        object {
            int cursorIndex = 0;
            public function next() returns record {|Iterable value;|}? {
                self.cursorIndex += 1;
                if (self.cursorIndex <= 2) {
                    return {
                        value: new Iterable()
                    };
                } else {
                    return ();
                }
            }
        } sample = new;
        return sample;
    }
};

public function testNestedIterableObject() returns int[] {
    AnotherIterable p = new AnotherIterable();
    int[] integers = from var itr in p
                     from var item in itr
                     select item;

    return integers;
}

type IterableWithError object {
    public function __iterator() returns abstract object {

        public function next() returns record {|int value;|}|error?;
    } {
        object {
            int[] integers = [12, 34, 56, 34, 78];
            int cursorIndex = 0;
            public function next() returns record {|int value;|}|error? {
                self.cursorIndex += 1;
                if (self.cursorIndex == 3) {
                    return error("Custom error thrown.");
                }
                else if (self.cursorIndex <= 5) {
                    return {
                        value: self.integers[self.cursorIndex - 1]
                    };
                } else {
                    return ();
                }
            }
        } sample = new;
        return sample;
    }
};

public function testIterableWithError() returns int[]|error {
    IterableWithError p = new IterableWithError();
    int[]|error integers = from var item in p
                     select item;

    return integers;
}

type NumberGenerator object {
    int i = 0;
    public function next() returns record {| int value; |}? {
        self.i += 1;
        if(self.i < 5) {
          return { value: self.i };
        }
    }
};

type NumberStreamGenerator object {
    int i = 0;
    public function next() returns record {| stream<int> value; |}? {
         self.i += 1;
         if (self.i < 5) {
             NumberGenerator numGen = new();
             stream<int> numberStream = new (numGen);
             return { value: numberStream};
         }
    }
};

public function testStreamOfStreams() returns int[] {
    NumberStreamGenerator numStreamGen = new();
    stream<stream<int>> numberStream = new (numStreamGen);
    record {| stream<int> value; |}? nextStream = numberStream.next();
    int[] integers = from var strm in numberStream
                     from var num in liftError(strm.toArray())
                     select num;

    return integers;
}

function liftError (int[]|error arrayData) returns int[] {
    if(!(arrayData is error)) {
        return arrayData;
    }
    return [];
}