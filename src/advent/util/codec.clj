(ns advent.util.codec)

;; source: https://stackoverflow.com/a/10087740/2338327
(defn utf8->hex
  "Encodes a UTF-8 string into a string of hexadecimal digits, I think in
   2-digit pairs?

   e.g. (utf8->hex \"hello\") => \"68656c6c6f\"
   h -> 68, e-> 65, l -> 6c, l -> 6c, o -> 6f"
  [s]
  (apply str (map #(format "%02x" %) (.getBytes s "UTF-8"))))

;; source: https://stackoverflow.com/a/10087740/2338327
(defn hex->utf8
  "Encodes a string of 2-digit hexadecimal numbers into a UTF-8 string.

   e.g. (hex->utf8 \"68656c6c6f\") => \"hello\"
   68 -> h, 65 -> e, 6c -> l, 6c -> l, 6f -> o"
  [s]
  (let [bytes (into-array Byte/TYPE
                (map (fn [[x y]]
                       (unchecked-byte (Integer/parseInt (str x y) 16)))
                     (partition 2 s)))]
    (String. bytes "UTF-8")))

