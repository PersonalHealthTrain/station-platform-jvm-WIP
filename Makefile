.PHONY: all
all:
	docker build --no-cache --pull --rm -t personalhealthtrain/station-platform:1.0.0 .
