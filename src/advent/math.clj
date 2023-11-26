(ns advent.math
  (:require [clojure.math.combinatorics :as combo]))

(defn divisible-by?
  [x y]
  (zero? (rem x y)))

(defn prime-factors
  [n]
  (if (<= n 1)
    ()
    (let [factor (as-> n ?
                   (Math/sqrt ?)
                   (Math/ceil ?)
                   (inc ?)
                   (range 3 ? 2)
                   (cons 2 ?)
                   (filter #(divisible-by? n %) ?)
                   (first ?)
                   (or ? n))]
      (concat [factor] (prime-factors (/ n factor))))))

(defn all-factors
  [n]
  (if (zero? n)
    '(0)
    (->> n prime-factors combo/subsets (map #(apply * %)) sort)))

(defn last-n-bits
  "Returns the number whose binary representation is the last `n` bits of the
   provided number `value`.

   Example:

      The number 1092455 in binary is 100001010101101100111. (21 bits)

      The last 16 bits of that are 1010101101100111, which is 43879.

      Therefore, (last-n-bits 16 1092455) returns 43879.

   Bitwise arithmetic explanation:

      Bitwise AND (&, or `bit-and` in Clojure) can be used to \"mask\" a binary
      number, i.e. clear out all the bits that we don't want. In the case of the
      example above, we want to keep only the last 16 bits, so we can use the
      mask 1111111111111111. Any preceding bits are logically 0. So when we use
      bitwise AND to apply this mask to 1092455 in binary, it looks like this:

      Mask:   000001111111111111111
      Value:  100001010101101100111 (1092455)
      ----------------------------
      Result: 000001010101101100111 (43879)"
  [n value]
  (let [mask (-> (apply str (repeat n \1))
                 (Long/parseLong 2))]
    (bit-and mask value)))
