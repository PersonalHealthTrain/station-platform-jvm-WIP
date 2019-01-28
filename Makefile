.PHONY: all
all:
	docker build --no-cache --pull --rm -t personalhealthtrain/station-platform:latest .
	docker tag personalhealthtrain/station-platform:latest personalhealthtrain/station-platform:0.9.2
