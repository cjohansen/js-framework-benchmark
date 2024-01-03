(ns demo.utils)

(def adjectives ["pretty", "large", "big", "small", "tall", "short", "long", "handsome", "plain", "quaint", "clean", "elegant", "easy", "angry", "crazy", "helpful", "mushy", "odd", "unsightly", "adorable", "important", "inexpensive", "cheap", "expensive", "fancy"])
(def colours ["red", "yellow", "blue", "green", "pink", "brown", "purple", "brown", "white", "black", "orange"])
(def nouns ["table", "chair", "house", "bbq", "desk", "car", "pony", "cookie", "sandwich", "burger", "pizza", "mouse", "keyboard"])

(defn build-data [data id-atom count]
  (->> (range count)
       (reduce
        (fn [data _]
          (conj! data {:id (swap! id-atom inc)
                       :label (str (rand-nth adjectives) " " (rand-nth colours) " " (rand-nth nouns))}))
        (transient (or data [])))
       persistent!))

(defn update-some [data]
  (->> (range 0 (count data) 10)
       (reduce (fn [data index]
                 (let [row (get data index)]
                   (assoc! data index (assoc row :label (str (:label row) " !!!")))))
               (transient data))
       persistent!))

(defn swap-rows [data]
  (if (> (count data) 998)
    (-> data
        (assoc 1 (get data 998))
        (assoc 998 (get data 1)))
    data))

(defn delete-row [data id]
  (->> data
       (reduce
        (fn [data x]
          (cond-> data
            (not (identical? id (:id x)))
            (conj! x)))
        (transient []))
       persistent!))
