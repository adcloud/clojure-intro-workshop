(ns clojure-intro-workshop.core
  (:use quil.core))
;
;(defn create-world
;  "Return a vector of vectors that builds a 2D field
;  with the given width and height, randomly populated with 0 and 1"
;  [width height]
;  [[0 1 1 0 1]
;   [1 0 1 0 1]
;   [0 1 0 1 0]
;   [0 1 1 0 1]])
;
(defn create-world
  "Return a vector of vectors that builds a 2D field
  with the given width and height, randomly populated with 0 and 1"
  [width height]
  (for [_ (range height)]
    (for [_ (range width)]
      (rand-int 2))))

(defn world-width [world]
  (count (nth world 0)))

(defn world-height [world]
  (count world))

(defn element-at [world [x y]]
  (nth (nth world y) x))


(defn update-world [world]
  ; This is your chance to change the world! ;)
  world) ;; return new state
  

(defn tick [world]
    (Thread/sleep 5000)
    (send *agent* update-world)
    (send *agent* tick)
    world
  )

;; seting up state
;; --------------------------
(def world (agent (create-world 50 40)))
(send world tick)

;; rendering functions
;; --------------------------
(defn setup []
  (smooth)
  (frame-rate 20)
  (background 0)) ;; black background

(defn draw []
  (stroke 0)
  (stroke-weight 0)
  (let [tile-width (/ (width) (world-width @world))
        tile-height (/ (height) (world-height @world))]
    (dorun ;; force evaluation of lazy sequenz
      (for [x (range (world-width @world))
            y (range (world-height @world))]
        (do ;; we do our drawing side effect here
          ;; set fill based on cell alive state
          (fill (* 255 (element-at @world [x y])))
          ;; draw a rect for each cell
          (rect (* x tile-width) (* y tile-height)
                tile-width tile-height))))))


;; main function
;; --------------------------
(defn -main [& args]
  (defsketch example
  :title "Sketch"
  :setup setup
  :draw draw
  :size [(* (world-width @world) 15) (* (world-height @world) 15)]))
