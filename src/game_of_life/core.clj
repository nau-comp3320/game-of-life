(ns game-of-life.core
  (:gen-class))

(def game-board-size-width 10)
(def game-board-size-height 10)

(defn create-new-board
  "Creates a new gameboard with the given dimensions"
  [width height]
  (into {}
        (for [x (range width)
              y (range height)]
          (let [alive? (rand-nth [true false false])]
            [[x y] alive?]))))

(defn new-game
  "Returns an initial game state."
  [width height]
  {:width width
   :height height
   :board (create-new-board width height)})

(defn display-board
  "Renders the game to the console."
  [{:keys [height width board]}]
  (doseq [y (range height)]
    (doseq [x (range width)]
      (let [pos [x y]
            alive? (board pos)]
        (print (if alive? \X \space))))
    (println)))

(defn get-neighbors
  "Given a position, get all neighboring positions."
  [[x y]]
  (for [nx (range (dec x) (+ x 2))
        ny (range (dec y) (+ y 2))
        :when (not (and (= x nx) (= y ny) ))]
    [nx ny]))

(defn num-live-neighbors
  "Given a board and a position on the board, return the
   number of live neighbors for that positions within the board."
  [board pos]
  (let [neighbors (get-neighbors pos)]
    (count (filter true? (map board neighbors)))))

(defn create-position-rule-applier
  [board]
  (fn [[pos alive?]]
    (cond
      (and alive?
           (> 2 (num-live-neighbors board pos)))
        [pos false]
      (and alive?
           (>= 3 (num-live-neighbors board pos) 2))
        [pos true]
      (and alive?
           (> (num-live-neighbors board pos) 3))
      [pos false]
      (and (not alive?)
         (= (num-live-neighbors board pos) 3))
        [pos true])))

(defn apply-rules
  [{:keys [board] :as state}]
  (let [new-board (into {} (map (create-position-rule-applier board)
                                board))]
    (assoc state :board new-board)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [game (new-game 30 30)]
    (println)
    (println)
    (println)
    (println)
    (display-board game)
    (Thread/sleep 500)
    (recur (apply-rules game))))
