module.exports = {
    client: {
        service: {
            name: "test-schema",
            localSchemaFile: "schema.json"
        },
        includes: [ "src/main/graphql/*.graphql" ]
    }
};