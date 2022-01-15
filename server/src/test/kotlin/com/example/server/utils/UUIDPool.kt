package com.example.server.utils

class UUIDPool(private val ids: List<String>, private var nextId: Int = 0) {
    companion object {
        private val sampleIdx = listOf(
            "93a3acdc-6b2c-410a-a798-46410217f125",
            "201f47a8-45f0-4c5b-ad30-ed55b225207e",
            "d103d15d-387e-4855-9241-508e93180e9d",
            "6b95661c-3374-40da-8d9f-ec914b9e9873",
            "d68fb6f8-7b33-4a04-a547-2066dcaa95eb",
            "05f7e627-2091-4e3a-b7e2-e61e282c5d77",
            "e983e272-9283-4550-b1ca-b20c1b708f63",
            "e8c4a1ca-89a2-4c95-8795-4f049891a4d2",
            "25314b8f-0ae4-4c88-86ea-ebbc918a834a",
            "08e1ac36-3de3-4e8f-874b-689068a7e3f3",
            "cb102702-55cd-4fd0-a181-ebc9fd6f8f3d",
            "6c2859eb-6ca6-471c-9721-c686fe89cf8e",
            "12f303bc-cbe6-4cd3-8341-fa97a1f9338e",
            "751452fb-b2af-447b-a7b7-244d8e88cdd2",
            "7fdc8503-ae73-472f-9d9f-7416867388a1",
            "bdd23c50-2277-41f7-9d99-17840a2defc5",
            "5e43df4c-7129-4d17-86b1-d177e2c1d81a",
            "501d1def-c7cb-4c33-8412-a6d25a96882a",
            "fefbf6e6-8257-492d-af55-8ea367b6da5c",
            "9d62022c-47aa-40df-aa2f-6137d8c8728d"
        )

        fun getPool(): UUIDPool = UUIDPool(sampleIdx)
    }

    fun getId(): String {
        if (nextId >= ids.size) {
            throw IllegalAccessError()
        }
        return ids[nextId++]
    }
}
