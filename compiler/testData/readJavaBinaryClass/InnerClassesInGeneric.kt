package test

public open class InnerClassesInGeneric<P, Q>() : java.lang.Object() {
    public open class Inner() : java.lang.Object() {
    }
    
    public open class Inner2() : Inner() {
        public open fun iterator() : jet.Iterator<P>? {
            throw UnsupportedOperationException()
        }
    }
}
