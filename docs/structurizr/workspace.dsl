workspace {
    !identifiers hierarchical
	
    model {
        u = person "User"
        a = person "Admin"
        ss = softwareSystem "SQS-Chat-App-System" {
            tags "Owned"
            wa = container "Web-Application"{
                tags "Frontend"
                tags "Owned"
            }
            sbs = container "SpringBoot-Server"{
                tags "Backend"
                tags "Owned"

                jts = component "JwtService"
                btf = component "BearerTokenFilter"
                sc = component "securityConfig"

                ac = component "AuthController"
                uc = component "UserController"
                us = component "UserService"
                ur = component "UserRepo"
                mc = component "MessageController"
                ms = component "MessageService"
                mr = component "MessageRepo"
                cc = component "CommentController"
                cs = component "CommentService"
                cr = component "CommentRepo"
                qs = component "Quote-Service"
            }
            db = container "MySQL-Server" {
                tags "Database"
                tags "Owned"
            }
        }
        qa = softwareSystem "Quote-API (zenquotes.io)" {
            tags "API"
        }
        a -> ss "Starts / Controls / Stops"
        u -> ss.wa "Uses"
        ss.wa -> ss.sbs.ac "REST-Requests"
        ss.wa -> ss.sbs.mc "REST-Requests"
        ss.wa -> ss.sbs.cc "REST-Requests"

        ss.sbs.uc -> ss.sbs.us "Calls"
        ss.sbs.us -> ss.sbs.ur "Calls"
        ss.sbs.ur -> ss.db "Queries"

        ss.sbs.mc -> ss.sbs.ms "Calls"
        ss.sbs.ms -> ss.sbs.mr "Calls"
        ss.sbs.ms -> ss.sbs.qs "Calls"
        ss.sbs.mr -> ss.db "Queries"

        ss.sbs.cc -> ss.sbs.cs "Calls"
        ss.sbs.cs -> ss.sbs.cr "Calls"
        ss.sbs.cs -> ss.sbs.ms "Calls"
        ss.sbs.cr -> ss.db "Queries"

        ss.sbs.qs -> qa "REST-Requests"
    }

    views {
        systemContext ss "System-Context" {
            include *
            autolayout lr
        }

        container ss "Container" {
            include *
            autolayout lr
        }

        component ss.sbs "Component-Backend" {
            include *
            exclude ss.sbs.jts ss.sbs.btf ss.sbs.sc
            autoLayout tb
        }

        component ss.sbs "Component-Backend-Extra" {
            include ss.sbs.jts ss.sbs.btf ss.sbs.sc
            autoLayout tb
        }

        styles {
            element "Owned" {
                background MediumSeaGreen
            }

            element "Person" {
                background SlateBlue
                shape person
            }
            element "Frontend" {
                shape webbrowser
            }
            element "API" {
                shape Diamond
            }
            element "Database" {
                shape cylinder
            }
            element "Component" {
                background MediumSeaGreen
            }
        }
    }
}