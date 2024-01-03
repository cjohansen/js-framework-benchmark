(ns demo.main
  (:require [demo.utils :as u]
            [replicant.dom :as d]))

(defn Row [{:keys [id label selected?]}]
  [:tr {:replicant/key id
        :class (when selected? "danger")}
   [:td.col-md-1 id]
   [:td.col-md-4
    [:a {:on {:click [::select-row id]}}
     label]]
   [:td.col-md-1
    [:a {:on {:click [::delete-row id]}}
     [:span.glyphicon.glyphicon-remove
      {:aria-hidden "true"}]]]
   [:td.col-md-6]])

(def toolbar
  [:div.jumbotron
   [:div.row
    [:div.col-md-6
     [:h1 "Replicant"]]
    [:div.col-md-6
     [:div.row
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "run"
         :on {:click [::run]}}
        "Create 1,000 rows"]]
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "runlots"
         :on {:click [::run-lots]}}
        "Create 10,000 rows"]]
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "add"
         :on {:click [::add]}}
        "Append 1,000 rows"]]
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "update"
         :on {:click [::update-some]}}
        "Update every 10th row"]]
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "clear"
         :on {:click [::clear]}}
        "Clear"]]
      [:div.col-sm-6.smallpad
       [:button.btn.btn-primary.btn-block
        {:type "button"
         :id "swaprows"
         :on {:click [::swap-rows]}}
        "Swap rows"]]]]]])

(defn Main [data]
  [:div.container
   toolbar
   [:table.table.table-hover.table-striped.test-data
    [:tbody
     (for [row (:rows data)]
       (Row (cond-> row
              (= (:selected data) (:id row))
              (assoc :selected? true))))]]
   [:span.preloadicon.glyphicon.glyphicon {:remove
                                           {:aria-hidden "true"}}]])

(def id-atom (atom 0))
(def data (atom {}))

(d/set-dispatch!
 (fn [_re action]
   (case (first action)
     ::run
     (reset! data {:rows (u/build-data [] id-atom 1000)})

     ::run-lots
     (reset! data {:rows (u/build-data [] id-atom 10000)})

     ::add
     (swap! data update :rows u/build-data id-atom 1000)

     ::update-some
     (swap! data update :rows u/update-some)

     ::clear
     (reset! data {})

     ::swap-rows
     (swap! data update :rows u/swap-rows)

     ::select-row
     (swap! data assoc :selected (second action))

     ::delete-row
     (swap! data update :rows u/delete-row (second action)))))

(defn render []
  (d/render (.getElementById js/document "main") (Main @data)))

(add-watch data ::main (fn [_ _ _ _] (render)))

(render)
