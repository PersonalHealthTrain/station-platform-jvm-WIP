.PHONY: all
all:
	docker build --no-cache --pull --rm -t personalhealthtrain/station:latest .


