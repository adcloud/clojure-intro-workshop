;; 'variables'
;; --------------
(def var-name "string value")
(def var-name 2) ;; Integer value
(def var-name 2.0) ;; Float value
(def var-name 1/2) ;; Ratio value

;; define a vector
(def some-vector [1 2 3])
;; access a vector
(first some-vector)  ;; 1
(last some-vector)   ;; 3
(nth some-vector 1)  ;; 2

;; , (comma) are treated as whitespace
(def some-vector [1, 2,,, 3]) ;; [1 2 3]

;; changing a vector
(cons :a [2 3]) ;; [:a 2 3]
(conj [1 2] :c) ;; [1 2 :c]
(assoc [1 2] 0 :a) ;; [:a 2]

;; define a map
(def some-map {:key "value"})
(def some-map-2 {2 [:val1 "val2"]})
;; accessing a map
(get some-map :key) ;; "value"
(:key some-map)     ;; "value"
(some-map :key)     ;; "value"
(get some-map :missing-key "default") ;; "default"
(vals some-map) ;; "value"

;; changing a map
(assoc some-map :other-key "other value") ;; {:key "value", :other-key "other value"}




;; functions
;; --------------
(defn fn-name [arg1 arg2]
  (+ 1 2))

;; calling functions
(+ 1 2)   ;; 3
(+ 1 2 3) ;; 6

(fn-name 2 3) ;; 5

;; printing
(println "some " "more " "test " 3)

;; define local vars
(defn authorized? [user-id]
  (let [user (db-get-user user-id)])
    (authenticate user))

;; destructuring vectors
(defn get-element [[x y]]
  (nth x
    (nth y [[0 1]
            [2 3]])))
(def pos [1 1])
(get-element pos) ;; 3

;; destructuring maps
(defn elecet-user [{user-name :name}]
  (str user-name " for president"))
(elecet-user {:id 2, :name "Itchy"}) ;; "Itchy for president"




;; data transformation
;; --------------
;; map
(map inc [1 2 3])       ;; [2 3 4]
(map (fn [x] (inc x))
     [1 2 3])           ;; [2 3 4]

;; filter
(filter even? [1 2 3 4]) ;; [2 4]
(filter (fn [x] (even? x))
        [1 2 3 4]) ;; [2 4]

;; reduce
(reduce + [1 2 3]) ;; 6
(reduce (fn [accumelator x] (+ accumelator x))
        [1 2 3]) ;; 6

;; list comprehension
(for [user ["Itchy" "Scratchy"]]
  (str user " for president!"))
;; ["Itchy for president!" "Scratchy for president!"]

(for [x [0 1]
      y [0 1]]
    [x y]) ;; [[0 0] [0 1] [1 0] [1 1]]




;; flow controll
;; --------------

;; if
(if (= 1 1)
  (println "true case")
  (println "false case"))

;; condition
(cond
  (= 1 2) "1 == 2 case"
  (= 1 1) "1 == 1 case"
  :else "default case")


;; side-effects
;; --------------
(do
  (println "printing is a side efect")
  "value")
;; prints: printing is a side efect
;; returns "value"

