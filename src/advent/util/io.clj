(ns advent.util.io)

(defn char-seq
  "Returns a lazy sequence of characters from a reader.

   Stolen from: https://stackoverflow.com/a/11671362/2338327"
  [^java.io.Reader rdr]
  (let [chr (.read rdr)]
    (if (>= chr 0)
      (cons (char chr) (lazy-seq (char-seq rdr))))))

