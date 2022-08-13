//
// Created by Etienne Caron on 2022-07-30.
//
import libCommon

/**
 sealed-class to enum example
 TODO: Reuse for AuthState?
 */
public enum SealedClassToEnumExample {
    case bar(SCFoo.SCBar)
    case baz(SCFoo.SCBaz)

    public init(_ sc: SCFoo) {
        if let sc = sc as? SCFoo.SCBar {
            self = .bar(sc)
        } else if let sc = sc as? SCFoo.SCBaz {
            self = .baz(sc)
        } else {
            fatalError("🐭 Oh no! 🍦")
        }
    }
}
