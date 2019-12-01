package todolist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import spark.Spark.get
import spark.Spark.post
import spark.Spark.patch
import spark.Spark.delete

fun main(args: Array<String>) {
    val objectMapper = ObjectMapper().registerKotlinModule()
    val jsonTransformer = JsonTransformer(objectMapper)
    val taskRepository = TaskRepository()
    val taskController = TaskController(objectMapper, taskRepository)

    // TODO pathのグルーピングしたい
    get("/tasks", taskController.index(), jsonTransformer)
    get("/tasks/:id", taskController.show(), jsonTransformer)
    post("/tasks", taskController.create(), jsonTransformer)
    patch("/tasks/:id", taskController.update(), jsonTransformer)
    delete("/tasks/:id", taskController.destroy(), jsonTransformer)
}

// curl -s http://localhost:4567/tasks | jq
// curl -s http://localhost:4567/tasks/1 | jq
// curl -s http://localhost:4567/tasks -X POST -d '{"content":"旅行の計画をする"}' | jq
// curl -s http://localhost:4567/tasks/1 -X PATCH -d '{"content":"夏の旅行の計画をする"}' -v
// curl -s http://localhost:4567/tasks/1 -X PATCH -d '{"done":true}' -v
// curl -s http://localhost:4567/tasks/1 -X DELETE -v
