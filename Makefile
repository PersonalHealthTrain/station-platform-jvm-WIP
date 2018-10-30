.PHONY: all
all:
	docker build --no-cache --pull --rm -t personalhealthtrain/station:latest .
	docker tag personalhealthtrain/station:latest personalhealthtrain/station:0.0.4
