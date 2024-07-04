(config
    (text-field
        :name         "clientId"
        :label        "Enter Your Client Id"
        :placeholder  ""
        :required     true)
    (password-field
        :name         "clientSecret"
        :label        "Client Secret"
        :placeholder  "Enter Your Client Secret"
        :required     true)
    (text-field
        :name "subDomain"
        :label "Domain name"
        :placeholder "Sub Domain name")   
    (oauth2/refresh-token-with-client-credentials
    :api-auth-fields "accessToken"
        (access-token
            (source
                (http/post
                    :url "https://{subDomain}.ilevelsolutions.com/v1/token"
                    (header-params
                        "Authorization" "Basic BASE_64({clientId}:{clientSecret})")
                    (body-params
                        "grant-type" "client_credentials")))
            (fields
                access-token :<= "access_token"
                token-type :<= "token_type"
                refresh-token :< "refresh_token"
                scope 
                realm-id :<= "realmId"
                expires-in :<= "expires_in"))))
(default-source (http/get :base-url "https://{subDomain}.ilevelsolutions.com/v1"
        (header-params 
                    "Accept" "application/json"
                    "content type" "application/x-www-form-urlencoded"
                    "api-version"  1.0))
            (paging/Page-number :page-number-query-param-initial-value
                                :page-number-query-param-name
                                :limit
                                :limit-query-param-name)
            (auth/oauth2)
            (error-handler
                (when :status 400 :message "Bad Request")
                (when :status 401 :message "Unauthorized":action refresh )
                (when :status 403 :message "Forbidden")
                (when :status 404 :message "not found")
                (when :status 405 :message "Method not allowed")
                (when :status 409 :message "conflict")
                (when :status 422 :message "Unprocessable Entity")
                (when :status 429 :message "Too Many Requests")
                (when :status 500 :message "Internal Server Error")))
(temp-entity securities
        (api-docs-url "file:///C:/Users/SrujanAnupa/Downloads/iLEVEL%20API%20Docs.pdf")
        (source (http/get : url "/securities")
                (setup-test
                  (upon-receiving :code 200 (pass)))
                  (extract-path "data")) 
        (fields
          id : id))

