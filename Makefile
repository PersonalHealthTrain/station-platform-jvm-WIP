.PHONY: all
all:
	docker build --no-cache --pull --rm -t personalhealthtrain/station:0.0.4 .

