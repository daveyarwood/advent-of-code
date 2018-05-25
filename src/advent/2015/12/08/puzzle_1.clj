(ns advent.2015.12.08.puzzle-1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; source: https://stackoverflow.com/a/10087740/2338327
(defn hexify [s]
  (apply str (map #(format "%02x" %) (.getBytes s "UTF-8"))))

;; source: https://stackoverflow.com/a/10087740/2338327
(defn unhexify [s]
  (let [bytes (into-array Byte/TYPE
                 (map (fn [[x y]]
                        (unchecked-byte (Integer/parseInt (str x y) 16)))
                      (partition 2 s)))]
    (String. bytes "UTF-8")))

(defn count-extra-chars
  [s]
  (let [total-chars  (count s)
        string-chars (-> s
                         (str/replace #"\\x([0-9a-f]{2})"
                                      (fn [[_ match]] (unhexify match)))
                         (str/replace #"\\\"" "\"")
                         (str/replace #"\\\\" (str/re-quote-replacement "\\"))
                         count
                         ;; don't count the initial and final "
                         (- 2))]
    (- total-chars string-chars)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-08-01-input"))]
    (->> rdr line-seq (map count-extra-chars) (reduce +) prn)))

