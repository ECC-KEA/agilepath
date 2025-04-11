.PHONY: dev ci prod stop clean nuke

dev:
	docker compose --profile dev up --build --watch

ci:
	docker compose --profile ci up --build -d

prod:
	docker compose --profile prod up --build -d

stop:
	docker compose --profile dev --profile ci --profile prod down

clean:
	docker system prune -f

nuke:
	docker system prune -af --volumes