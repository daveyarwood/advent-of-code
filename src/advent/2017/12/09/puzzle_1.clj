(ns advent.2017.12.09.puzzle-1
  (:require [advent.util.io  :as util]
            [clojure.java.io :as io]))

;; I get a StackOverflowError if I don't fully realize the sequence as I'm going
;; by ensuring that I'm working with a vector. One of Clojure's quirks.
(defn- concatv
  [& args]
  (vec (apply concat args)))

(defn- ex-throw
  [& args]
  (throw (apply ex-info args)))

(defn tokenize-end
  [{:keys [buffer tokens nesting-level] :as context}]
  (when (seq buffer)
    (ex-throw "Leftover characters in buffer." context))
  (when (pos? nesting-level)
    (ex-throw "Unterminated group." context))
  tokens)

(defn- unexpected-char!
  [c context]
  (ex-throw (format "Unexpected '%c'" c) context))

(defn tokenize
  ([characters]
   (tokenize characters {:tokens [], :buffer "", :nesting-level 0}))
  ([[c & more]
    {:keys [ignore? garbage? buffer nesting-level] :as context}]
   (cond
     (contains? #{nil \newline} c)
     (tokenize-end context)

     ignore?
     (recur more (dissoc context :ignore?))

     (= \! c)
     (if (and (seq more) (not= \newline (first more)))
       (recur more (assoc context :ignore? true))
       (unexpected-char! \! context))

     (= \> c)
     (recur more (-> context
                     (dissoc :garbage?)
                     (update :tokens concatv
                             [[:garbage nesting-level buffer]])
                     (assoc :buffer "")))

     garbage?
     (recur more (update context :buffer str c))

     (= \< c)
     (recur more (-> context
                     (assoc :garbage? true)
                     (update :tokens concatv
                             (when (seq buffer)
                               [[:content nesting-level buffer]]))
                     (assoc :buffer "")))

     (= \{ c)
     (recur more
            (-> context
                (update :tokens concatv
                        (remove nil?
                                [(when (seq buffer)
                                   [:content nesting-level buffer])
                                 [:group-start
                                  (inc nesting-level)]]))
                (assoc :buffer "")
                (update :nesting-level inc)))

     (= \} c)
     (if (pos? nesting-level)
       (recur more
              (-> context
                  (update :tokens concatv
                          (remove nil?
                                  [(when (seq buffer)
                                     [:content nesting-level buffer])
                                   [:group-end nesting-level]]))
                  (assoc :buffer "")
                  (update :nesting-level dec)))
       (unexpected-char! \} context))

     :else
     (recur more (update context :buffer str c)))))

(defn total-group-score
  [tokens]
  (->> (for [[token-type nesting-level] tokens
             :when (= :group-end token-type)]
         nesting-level)
       (apply +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-09-01-input"))]
    (->> rdr util/char-seq tokenize total-group-score prn)))
