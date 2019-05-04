module.exports = {
    client: {
        service: {
            name: "test-schema",
            localSchemaFile: "hello-world2/schema.json"
        },
        includes: [ "src/main/graphql/*.graphql" ]
    }
};