package todolist

import com.fasterxml.jackson.databind.ObjectMapper
import spark.Route
import spark.Spark.halt

class  TaskController(private val objectMapper: ObjectMapper,
                      private val taskRepository: TaskRepository) {

    fun index() : Route = Route { request, response ->
        taskRepository.findAll()
    }

    fun create() : Route = Route { request, response ->
        val request: TaskCreateRequest =
                try {
                    objectMapper.readValue(request.bodyAsBytes(), TaskCreateRequest::class.java)
                } catch (e: Exception) {
                    throw halt(400)
                }
        val task = taskRepository.create(request.content)
        response.status(201)
        task
    }

    fun show() : Route = Route { request, response ->
        val id = request.params("id").toLongOrNull()
        id?.let(taskRepository::findById) ?: throw halt(404)
    }

    // TODO showメソッドと同じ処理を共通化したい
    fun destroy() : Route = Route { request, response ->
        val id = request.params("id").toLongOrNull()
        val task = id?.let(taskRepository::findById) ?: throw halt(404)
        taskRepository.delete(task)
        response.status(204)
    }

    // TODO showメソッドと同じ処理を共通化したい
    fun update() : Route = Route { request, response ->
        val requestBody: TaskUpdateRequest =
            try {
                objectMapper.readValue(request.bodyAsBytes(), TaskUpdateRequest::class.java)
            } catch (e: Exception) {
                throw halt(400)
            }
        val id = request.params("id").toLongOrNull()
        val task = id?.let(taskRepository::findById) ?: throw halt(404)
        val newTask = task.copy(
            content = requestBody.content ?: task.content,
            done = requestBody.done ?: task.done
        )
        taskRepository.update(newTask)
        response.status(204)
    }
}
